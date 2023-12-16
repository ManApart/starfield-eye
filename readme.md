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

https://www.reddit.com/r/starfieldmods/comments/18jsbly/companion_app_starfield_eye_now_100/

Companion App: Starfield Eye Now 1.0.0

Starfield Eye is now in 1.0.0! There are more features than I can list here, so I'll just list the highlights and you can see the rest (or use the app ad free) at https://manapart.github.io/starfield-eye/#about

Player Progress
- Complete your own codex of the game's many flora, fauna, and planets
- Track your survey and scan progress
- Upload Screenshots

Planets
- Search using a wide range of criteria
- View a searchable galaxy map and system orrery views

Life Signs
- Search Flora and Fauna
- Complete your own pokedex

Outposts
- Track your outposts with screenshots and what resources are produced
- View outposts by resource
- One button click to travel in game (chart a course) to your outpost

And More
- Export your data
- Ad free
- No data sent to the server
- Most things link to the wiki




Captions

Search using a wide range of criteria
Searchable galaxy map
Track your quest progress and latest objective
View misc stats and some achievement progress
Mark stars discovered, planets landed on, and fauna/flora scan percent