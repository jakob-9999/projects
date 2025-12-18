import {useEffect, useState} from "react";

export function usePrecipitationData() {
    const [timeStepGroup, setTimeStepGroup] = useState({});

    useEffect(() => {
        fetch("/api/dmi-dini-bbox/get-grid")
            .then((res) => res.json())
            .then(data => {console.log(data)

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
    },[]);

    return timeStepGroup;
}