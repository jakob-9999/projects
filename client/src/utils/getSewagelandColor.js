export function getSewagelandColor(type) {

    if (type?.includes("Fælleskloakeret"))
        return "#0D114F";

    if (type?.includes("Separatkloakeret"))
        return "#003CFF";

    if (type?.includes("Spildevandskloakeret"))
        return "#5A7CFF";

    if (type?.includes("Ukloakeret"))
        return "#D6E0FF";

    return "#90A4C4";
}
