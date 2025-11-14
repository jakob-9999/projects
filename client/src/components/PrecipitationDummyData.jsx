import { Rectangle, Popup } from "react-leaflet";
import {useEffect} from "react";
import {useState} from "react";
import {geoJSON} from "leaflet";
import Box from '@mui/material/Box';
import Slider from '@mui/material/Slider';

// fetch grid cells
export default function PrecipitationLayer() {
    const [timeStepGroup, setTimeStepGroup] = useState({})
    const [sliderValue, setSliderValue] = useState(0)

    useEffect(() => {
        fetch("/api/dmi-dini-bbox/get-grid")
            .then((res) => {
                return res.json()
            })
            .then(data => {
                console.log(data)

                //group features with same "step" together
                //use reduce to accumulate all features one by one in an object called acc
                //acc is an accumulator-object (key-value pair): key=step and value=an array of features
                const timeStepGroup = data.features.reduce((acc, feature) => {
                    const step = feature.properties.step;
                    //if accumulated group of features does not exist for the given step, create empty list
                    if (!acc[step]) {
                        acc[step] = [];
                    }
                    //push the feature to the acc object.
                    //features with the same step-property get pushed to the same place in the acc object
                    acc[step].push(feature);
                    //acc has the following structure:
                    //"2025-11-13T03:00:00.000Z": [feature1, feature2, ... ]
                    return acc;
                }, {});

                //update state. timeStepGroup is not an empty object after update
                setTimeStepGroup(timeStepGroup);

            })
            .catch((err) => console.error("Error fetching geoJSON:", err));
    }, []);

    //returns an array of all keys (steps) from timeStepGroup
    const steps = Object.keys(timeStepGroup);
    //get features where slider value and timeStepGroup-index match
    //steps is an array, so steps[sliderValue] returns the step at the index that matches the slider
    //timeStepGroup[steps[sliderValue]] is an array of features for that step
    const currentStepFeatures = steps[sliderValue] ? timeStepGroup[steps[sliderValue]] : [];

    const getColor = (precipitation) => {
        if (precipitation > 10) return "blue";
        if (precipitation > 5) return "dodgerblue";
        if (precipitation > 0.5) return "lightblue";
        if (precipitation <= 0.5) return "dodgerblue"; //for test. remove later
        return "transparent";
    };

    function kmToDegreesOffset(latDeg, km) {
        const latOffset = km / 111.32; // 1 degree of latitude is approximately 111.32 km
        const lonOffset = km / (111.32 * Math.cos(latDeg * Math.PI / 180));
        return { latOffset, lonOffset };
    }

    const cellSizeKm = 2; // each grid is 2*2 km

    return (
        <>
            {/* sx is style extension. Lets us create a styling object. Needed to put slider on top of map with zIndex */}
            <Box
                sx={{
                    position: "absolute",
                    bottom: 20,
                    left: 20,
                    zIndex: 1000,
                    width: 1000, // just example width
                }}
            >
                {/* slider taken from https://mui.com/material-ui/react-slider/ */}
                <Slider
                    value={sliderValue}
                    aria-label="Time"
                    defaultValue={0}
                    onChange={(e, val) => setSliderValue(val)}
                    valueLabelDisplay="auto"
                    step={1}
                    marks
                    min={0}
                    max={60}
                />
            </Box>

            {currentStepFeatures.map((feature, i) => {
                // Extract coordinates (GeoJSON is [lon, lat])
                const [lon, lat] = feature.geometry.coordinates;
                const { latOffset, lonOffset } = kmToDegreesOffset(lat, cellSizeKm / 2);

                // Calculate grid corners
                const bounds = [
                    [lat - latOffset, lon - lonOffset],
                    [lat + latOffset, lon + lonOffset],
                ];

                // Extract precipitation value
                // ?? 0 is a safeguard in case the property is missing
                const value = feature.properties["total-precipitation"] ?? 0;
                const color = getColor(value);


                return (
                    <Rectangle
                        key={i}
                        bounds={bounds}
                        pathOptions={{
                            color: color,
                            fillColor: color,
                            fillOpacity: 0.3,
                        }}
                    >
                        <Popup>
                            <b>Rain:</b> {value} mm/h
                            <br />
                            <b>Time:</b> {feature.properties.step}
                        </Popup>
                    </Rectangle>
                );
            })}
        </>
    );
}
