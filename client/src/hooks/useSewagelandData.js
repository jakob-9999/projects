import { useEffect, useState } from "react";

export function useSewagelandData() {
    const [data, setData] = useState(null);

    useEffect(() => {
        fetch("/api/sewageland/features")
            .then((res) => res.json())
            .then((raw) => {
                setData(raw);
            })
            .catch((err) =>
                console.error("Error fetching Sewageland geoJSON:", err)
            );
    }, []);

    return data;
}