
export function getPrecipitationColor(precipitation) {
        if (precipitation > 10) return "blue";
        if (precipitation > 5) return "dodgerblue";
        if (precipitation > 0.5) return "lightblue";
        if (precipitation <= 0.5) return "dodgerblue"; //for test. remove later
        return "transparent";
}