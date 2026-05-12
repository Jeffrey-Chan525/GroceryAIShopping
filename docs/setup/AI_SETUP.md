Local AI Setup with Ollama
1. Installation
   Ollama runs as a background service that manages model weights and provides a local API.

Windows: Download and run the Ollama Windows Installer.

macOS: Download the Ollama macOS Build.

Linux: Run the following command in your terminal:

Bash
curl -fsSL https://ollama.com/install.sh | sh
Note for AMD Users: If you are using an AMD Radeon RX 9070, ensure you have the latest drivers installed. Ollama supports ROCm for AMD acceleration, which significantly speeds up inference compared to running on the CPU.

