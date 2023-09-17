# Starfield Eye

An Ad free searchable starmap with labels and bookmarks.

Extremely wip at this point.

https://manapart.github.io/starfield-eye/#galaxy

## Deploying
```
gradlew jsBrowserDistribution
#Switch to deploy branch in separate folder
cd ../starfield-eye-deploy
cp -r ../starfield-eye/build/dist/js/productionExecutable/* .
```

## Crew

- Star and Planet data from Starfield Game Files
- Resource Data from [Google Docs](https://docs.google.com/spreadsheets/d/1seE2vzP_8Whs43C-6CXpoHPyJMFGoUH4TkSzeJqMHm4/edit#gid=231618918), originoally pulled from [Hardcore Gamer](https://hardcoregamer.com/db/starfield-all-locations-systems-planets-moons/)
- Inorganic Resource Data from [Starfield Wiki](https://starfieldwiki.net/wiki/Starfield:Resources)