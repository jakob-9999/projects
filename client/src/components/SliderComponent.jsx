import Box from '@mui/material/Box';
import Slider from '@mui/material/Slider';

export default function SliderComponent({
sliderValue, setSliderValue, onSliderMouseUp,onSliderMouseDown}) {

    return (
        // sx is style extension. Lets us create a styling object. Needed to put slider on top of map with zIndex
        <Box
            sx={{
                position: "absolute",
                bottom: 20,
                left: 20,
                zIndex: 1000,
                width: 1000, // just example width
            }}
        >
             {/*slider taken from https://mui.com/material-ui/react-slider/ */}
            <Slider
                value={sliderValue}
                aria-label="Time"
                defaultValue={0}
                onChange={(e, val) => setSliderValue(val)}
                onMouseDown={onSliderMouseDown}
                onChangeCommitted={onSliderMouseUp}
                valueLabelDisplay="on"
                step={1}
                marks
                min={0}
                max={60}
            />
        </Box>
    )
}