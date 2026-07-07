
// exported to PrecipitationLayer
export function kmToDegreesOffset(latDeg, km) {
    const latOffset = km / 111.32; // 1 degree of latitude is approximately 111.32 km
    const lonOffset = km / (111.32 * Math.cos(latDeg * Math.PI / 180));
    return { latOffset, lonOffset };
}