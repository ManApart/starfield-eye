# Starfield Eye

[![Build and Test](https://github.com/ManApart/starfield-eye/actions/workflows/runTests.yml/badge.svg)](https://github.com/ManApart/starfield-eye/actions/workflows/runTests.yml)


An Ad free searchable starmap with labels and bookmarks.

Extremely wip at this point.

https://manapart.github.io/starfield-eye/#galaxy

## Deploying
```
./scripts/stage.sh
or
gradlew jsBrowserDistribution
#Switch to deploy branch in separate folder
cd ../starfield-eye-deploy
cp -r ../starfield-eye/build/dist/js/productionExecutable/* .
```

## Testing the Dock Server

```
curl -X POST -d "GetSFSEVersion" http://localhost:55555/console -v
```

## Crew

- Star and Planet data from Starfield Game Files
- Resource Data from [Google Docs](https://docs.google.com/spreadsheets/d/1seE2vzP_8Whs43C-6CXpoHPyJMFGoUH4TkSzeJqMHm4/edit#gid=231618918), originally pulled from [Hardcore Gamer](https://hardcoregamer.com/db/starfield-all-locations-systems-planets-moons/)
- Inorganic Resource Data from [Starfield Wiki](https://starfieldwiki.net/wiki/Starfield:Resources)


## Advertisement Text

Starfield Eye is an Ad Free, Searchable Starmap for Starfield.

Open it on a second monitor while playing, or on your phone to plan your next adventure.

Search for planets based on any combination of resources, traits, features, name and more. Easily find a planet for your outpost that contains Aluminum, Iron, and a mountain view.

Browse system by system with an Orrery that matches the in game UI.

Save your favorite planets or the ones you want to explore. Add the name of your outposts and then see them all by searching "outpost". All data is stored locally on your browser, so no login is required and nothing is tracked, but you can import and export data to back it up or share it between devices.

The Crew page also links to similar sites made by others, so you can pick the one that works best for you.

Please report any issues as github issues at https://github.com/ManApart/starfield-eye/