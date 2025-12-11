export function getSewagelandColor(type) {
    if (type?.includes("Fælleskloakeret"))
        return "#4A1F00";
    if (type?.includes("Separatkloakeret"))
        return "#B84D00";
    if (type?.includes("Spildevandskloakeret"))
        return "#FF7F32";
    if (type?.includes("Ukloakeret"))
        return "#B0B0B0";
    return "#888888";
}
