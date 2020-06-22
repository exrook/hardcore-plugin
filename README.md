Compiling
=========
Place the `spigot-api.jar` file in the same folder as this project, then run the `build.sh` script

### Auto-recompile on change
Requires inotify-tools
```
while inotifywait HardcorePlugin.java; do ./build.sh; done
```
