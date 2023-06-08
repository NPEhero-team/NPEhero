# Installation
1. Install Java 17 or newer
1. Go to Deployments > Releases.
1. Download the correct ZIP for your OS
1. Unzip it into an empty directory
1. Run the start file (start_linux.sh or start_windows.bat)

# Run in IDE
## VSCode
1. Windows: Install git for windows
1. import the repository...
- Method 1
    1. Click the blue clone button
    1. Click "Visual Studio Code (HTTPS)
- Method 2
    1. Click the source control button on the sidebar
    1. Click clone repository
    1. Return to GitLab and click the blue clone button
    1. Copy the "Clone with HTTPS" link
    1. Paste the link into the VSCode popup
3. Click the run and debug button on the sidebar 
1. Click "create launch.json" and open the file
1. Add the key "vmArgs" and put in the VM arguements for your OS

For example: 
```json
    "configurations": [
        {
            "type": "java",
            "name": "Driver",
            "request": "launch",
            "mainClass": "gui.Driver",
            "projectName": "GuitarHero",
            "vmArgs": "--module-path lib/linux --add-modules javafx.controls,javafx.fxml,javafx.media -Dprism.forceGPU=true"
        },
```
Do not forget the comma after the second to last item.

## Eclipse
1. Click the search icon in the upper right corner
2. Search "git repositories"
3. Click "Clone a git repository"
1. Return to GitLab and click the blue clone button
1. Copy the "Clone with HTTPS" link
1. Paste it into the URI box
1. Other information should fill in automatically, click next
1. Ensure that the Main branch is selected and click next
1. Change the directory to your eclipse workspace
1. Check "Import all existing Eclipse projects after clone finishes"
1. Click finish
1. Right click on project > Propereties
1. Select Run/Debug Settings
1. Select Driver
1. Click Edit
1. Click the Arguements tab
1. Paste in the VM Arguements for your OS

## Java VM Arguements
These are automatically run by the start files when installing from a ZIP.

#### Windows
`--module-path lib/windows/lib --add-modules javafx.controls,javafx.fxml,javafx.media`

#### Linux
`--module-path lib/linux --add-modules javafx.controls,javafx.fxml,javafx.media -Dprism.forceGPU=true`

# Development Links

[Figma link](https://www.figma.com/file/dpeMlWStSWrVHfLd0Uohws/Untitled?node-id=0%3A1&t=PVQi61Ig3AWtWNMm-1)

[JavaFX docs (with search)](https://openjfx.io/javadoc/15)

javafx guides (really helpful website):
[edencoding.com](https://edencoding.com/javafx-layouts/)
