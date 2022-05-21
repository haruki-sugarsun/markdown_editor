# markdown_editor
(wanna-be) Web-based Markdown Preview and Editor tool.

The original motivation to implement this tool was to write <https://github.com/haruki-sugarsun/manager-readme>.

----
![movie](https://github.com/haruki-sugarsun/markdown_editor/wiki/mov/movie.gif)
----

# Setup
The current implementation was tested with the following runtimes/environment:
* OpenJDK Runtime Environment Temurin-17.0.3+7 (build 17.0.3+7)
* Gradle 7.4.2

To test locally, you may simply run
```
$ ./gradlew run --args="-f README.md"
```
and open `http://127.0.0.1:8080/editor/index.html` with your browser.


# Dependency
We use <https://github.com/commonmark/commonmark-java> to render the Markdown, hence syntax support follows their implementation.
