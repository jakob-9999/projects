
export function getPrecipitationColor(precipitation) {
        if (precipitation > 10) return "darkblue";
        if (precipitation > 5) return "blue";
        if (precipitation > 0.5) return "dodgerblue";
        if (precipitation <= 0.5) return "transparent";
        return "transparent";
}