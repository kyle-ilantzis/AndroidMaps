# AndroidMaps - comparing google maps and mapbox performance on android

What is [google maps](https://developers.google.com/maps/)? Learn about the [google maps android sdk](https://developers.google.com/maps/documentation/android-api/)

What is [mapbox](https://www.mapbox.com/)? Learn about the [mapbox android sdk](https://www.mapbox.com/android-docs/map-sdk/overview/)

Does mapbox take less time than google maps when adding a big number of markers on the map?

## Experiments

Run on an a personal android device with:
* 1.7GHz Qualcomm® Snapdragon™ 615 Octa-core CPU
* Adreno 405 @ 550 MHz GPU
* 2GB RAM

Repeated 100 times

### Adding 1000 default markers to the map

These default markers provide no image to be displayed. 
The map then uses a default image to display the marker

Google Maps: on average 292.85 milliseconds

Mapbox: on average 54.19 milliseconds

![](readme/googlemaps_1000_points.png)

![](readme/mapbox_1000_points.png)

### Adding 1000 different markers to the map

The markers have an image associated with them to be displayed on the map.
We create a triangles with random colors for each marker. 

Google Maps:
* Creating the icons took on average 1209.57 milliseconds    
* Adding the markers took on average 314.31 milliseconds

Mapbox:
* Creating the icons took on average 354.42 milliseconds    
* Adding the markers took on average 446.01 milliseconds

![](readme/googlemaps_1000_different_points.png)

![](readme/mapbox_1000_different_points.png)

### Results

You can view the individual times of the experiments [here](readme/results.txt)

## Installation

1. git clone this repository
1. copy secrets.example.properties to secrets.properties
1. Enter your google maps and mapbox API keys in secrets.properties
1. Open with android studio
