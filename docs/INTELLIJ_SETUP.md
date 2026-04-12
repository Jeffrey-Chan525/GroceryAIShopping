# IntelliJ + Corretto + JavaFX Setup

1. Open IntelliJ IDEA.
2. Open the `smartspend-complete` folder.
3. Go to **File > Project Structure**.
4. Set **Project SDK** to **Amazon Corretto 21**.
5. Make sure the language level matches Java 21.
6. Let Maven import automatically, or reload Maven manually.
7. Open `MainApp.java` and run it.
8. If JavaFX dependencies do not resolve immediately, reload Maven projects.

## Maven Run Alternative
You can also run from the terminal:

```bash
mvn clean javafx:run
```
