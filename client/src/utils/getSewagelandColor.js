
export function getSewagelandColor(type) {
    if (type?.includes("Spildevandskloakeret")) return "red";
    if (type?.includes("samme")) return "yellow";
    if (type?.includes("Separatkloakeret")) return "green";
    return "#888";
}