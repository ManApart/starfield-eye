./gradlew jsBrowserDistribution && \
cp -r build/dist/js/productionExecutable/* ../starfield-eye-deploy/
cp -r build/dist/js/productionExecutable/* mod/Data/SFSE/plugins/sfse_plugin_console_web
cp build/dist/js/productionExecutable/index.html mod/Data/SFSE/plugins/sfse_plugin_console_web/starfield-eye.html
rm mod/starfield-eye.zip
zip -q -r mod/starfield-eye.zip mod/Data
