import Box from '@mui/material/Box';
import Slider from '@mui/material/Slider';
import {useEffect} from "react";
import {useState} from "react";

const currentHour = new Date().getHours();

export default function SliderComponent() {
    const [sliderValue, setSliderValue] = useState(0);
    const [precipitationData, setPrecipitationData] = useState([])
    const [allStepsData, setAllStepsData] = useState({});

    //get array of features
    useEffect(() => {
        fetch("/api/dmi-dini-bbox/get-grid")
            .then((res) => {
                return res.json()
            })
            .then(data => {
                console.log(data)

                //create new object to group features with the same time step
                //reduce takes an array and reduces it to the object timeStepGroup
                //reduce goes through each element in data.features
                const timeStepGroup = data.features.reduce((object, feature) => {
                    const step = feature.properties.step;
                    if (!object[step]) {
                        object[step] = [];
                    }
                    object[step].push(feature);
                    return object;
                }, {});

                //put group in allStepsData
                setAllStepsData(timeStepGroup);
            })
            .catch((err) => console.error("Error fetching geoJSON:", err));
    }, []);

    //get all features from current timestep
    const currentFeatures = allStepsData[sliderValue]

    //loop through each feature in currentFeatures
    currentFeatures.forEach(feature => {
        //get precipitationValue
        const precipitation = feature.properties["total-precipitation"]
        //get coordinates
        const coordinates = feature.geometry.coordinates
    });

    //slider taken from https://mui.com/material-ui/react-slider/
    function valuetext(value) {
        //return value as string
        return `${value}`;
    }



    return (
        <Box className="Slider">
            <Slider
                aria-label="Time"
                defaultValue={0}
                getAriaValueText={valuetext}
                valueLabelDisplay="auto"
                shiftStep={0}
                step={1}
                marks
                min={0}
                max={60}
                valueLabelFormat={(value) => {
                    const hour = (currentHour + value) % 24;
                    return `${hour}:00`;
                }}
            />
        </Box>
    );
}