import * as React from 'react';
import OpacityIcon from '@mui/icons-material/Opacity';
import LandscapeIcon from '@mui/icons-material/Landscape';
import ToggleButton from '@mui/material/ToggleButton';
import ToggleButtonGroup from '@mui/material/ToggleButtonGroup';
import Box from '@mui/material/Box';
import L from 'leaflet';
import { useEffect, useRef } from 'react';

export default function VerticalToggleButtons({
                                                  showSewage,
                                                  setShowSewage,
                                                  showPrecipitation,
                                                  setShowPrecipitation,
                                              }) {

    const boxRef = useRef(null);

    useEffect(() => {
        if (boxRef.current) {
            L.DomEvent.disableClickPropagation(boxRef.current);
        }
    }, []);

    return (

        <Box
            ref={boxRef}
            sx={{
                position: "absolute",
                bottom: 450,
                left: 10,
                zIndex: 1000,
                backgroundColor: "#fff",
            }}
        >

            <ToggleButtonGroup orientation="vertical" exclusive={false}>

                <ToggleButton
                    selected={showSewage}
                    onClick={() => setShowSewage(prev => !prev)}
                >
                    <LandscapeIcon />
                </ToggleButton>

                <ToggleButton
                    selected={showPrecipitation}
                    onClick={() => setShowPrecipitation(prev => !prev)}
                >
                    <OpacityIcon />
                </ToggleButton>

            </ToggleButtonGroup>
        </Box>
    );
}

