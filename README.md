# NPE Hero v2.0
One-year anniversary update!

Goals:
- [x] New integrated level editor
- [X] Custom keybindings
- [X] Accept mp3 song files
- [ ] UI improvements
- [ ] Code cleanup
- [X] Installer / Linux package

Todo - needs design:
- Improve errorList and error handling
- tweak game end
- make noteseditor2 resizeable

Todo - bugs:
- fix noteseditor1
- fix reliance on local font
- Fix notesEditor note preview being too small with no notes
- Properly center background image

# Building
### Run the app
Run the Driver class from your IDE (known to work with Intellij)

OR

Execute the maven goal javafx:run
- windows: `mvnw.cmd javafx:run`
- linux: `./mvnw javafx:run`

### Create an installer for your OS
Execute the maven package phase
- windows: `mvnw.cmd package`
- linux: `./mvnw package`

The installer executables should output to `target/dist`

# Development Links

[Figma link](https://www.figma.com/file/dpeMlWStSWrVHfLd0Uohws/Untitled?node-id=0%3A1&t=PVQi61Ig3AWtWNMm-1)

[JavaFX docs (with search)](https://openjfx.io/javadoc/15)

javafx guides (really helpful website):
[edencoding.com](https://edencoding.com/javafx-layouts/)
