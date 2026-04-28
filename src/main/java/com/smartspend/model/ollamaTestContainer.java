package com.smartspend.model;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Image;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.DockerClientFactory;
import org.testcontainers.containers.Container;
import org.testcontainers.ollama.OllamaContainer;
import org.testcontainers.utility.DockerImageName;

import java.io.IOException;
import java.util.List;


public class ollamaTestContainer {
    private static final Logger log = LoggerFactory.getLogger(ollamaTestContainer.class);

    static final String OLLAMA_IMAGE = "ollama/ollama:latest";
    static final String QWEN_MODEL = "qwen3.5:latest";
    static final String DOCKER_IMAGE_NAME = "tc-ollama/ollama:latest-qwen";

    public static void main(String[] args){
        // Creating the ollama container
        DockerImageName dockerImageName = DockerImageName.parse(OLLAMA_IMAGE);
        DockerClient dockerClient = DockerClientFactory.instance().client();
        List<Image> images = dockerClient.listImagesCmd().withReferenceFilter(DOCKER_IMAGE_NAME).exec();
        OllamaContainer ollama;
        if (images.isEmpty()){
            ollama = new OllamaContainer(dockerImageName);
        } else{
            ollama = new OllamaContainer(DockerImageName.parse(DOCKER_IMAGE_NAME).asCompatibleSubstituteFor(OLLAMA_IMAGE));
        }
        ollama.start();

        // pull the model and create an image based on the model
        try{
            log.info("Start pulling the '{}' model ... should take serveral minutes ...", QWEN_MODEL);
            Container.ExecResult r = ollama.execInContainer("ollama", "pull", QWEN_MODEL);
            log.info("Model pull completed. {}", r );

        } catch (IOException| InterruptedException e) {
            throw new RuntimeException("Error pulling model", e);
        }

        //Build the model
        ChatModel model = OllamaChatModel.builder()
                .baseUrl(ollama.getEndpoint())
                .temperature(0.0)
                .logRequests(true)
                .logResponses(true)
                .modelName(QWEN_MODEL)
                .build();
        // using the model
        String example = model.chat("say hello world");
        System.out.println(example);


        // stoping the ollama container
        ollama.stop();
    }
}
