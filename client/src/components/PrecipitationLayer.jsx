import { Rectangle, Popup } from "react-leaflet";
import { useState } from "react";
import SliderComponent from "./SliderComponent";
import { usePrecipitationData } from "../hooks/usePrecipitationData";
import { getPrecipitationColor} from "../utils/getPrecipitationColor.js";
import { kmToDegreesOffset } from "../utils/coordinateUtils";

// fetch grid cells
export default function PrecipitationLayer({ onSliderMouseDown, onSliderMouseUp}) {
    const [sliderValue, setSliderValue] = useState(0)
    const timeStepGroup = usePrecipitationData();

    //returns an array of all keys (steps) from timeStepGroup
    const steps = Object.keys(timeStepGroup);

    //get features where slider value and timeStepGroup-index match
    //steps is an array, so steps[sliderValue] returns the step at the index that matches the slider
    //timeStepGroup[steps[sliderValue]] is an array of features for that step
    const currentStepFeatures = steps[sliderValue] ? timeStepGroup[steps[sliderValue]] : [];

    const cellSizeKm = 2; // each grid is 2*2 km

    return (
        // pass slider events direct to the slider component
        <div>
            <SliderComponent
                sliderValue={sliderValue}
                setSliderValue={setSliderValue}
                onSliderMouseDown={onSliderMouseDown}
                onSliderMouseUp={onSliderMouseUp}
            />
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
                const color = getPrecipitationColor(value);


                return (
                    <Rectangle
                        key={i}
                        bounds={bounds}
                        pathOptions={{
                            stroke: false,
                            color: color,
                            fillColor: color,
                            fillOpacity: 0.5,
                        }}
                    >
                    </Rectangle>
                );
            })}
        </div>
    );
}
