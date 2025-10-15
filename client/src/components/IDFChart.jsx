import React from 'react';
import {LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend} from 'recharts';

const data = [
    { rainfallDuration: 0.01, "2-year": 100, "5-year": 75, "10-year": 60 },
    { rainfallDuration: 0.1, "2-year": 200, "5-year": 150, "10-year": 120 },
    { rainfallDuration: 1, "2-year": 400, "5-year": 300, "10-year": 250 },
    { rainfallDuration: 10, "2-year": 700, "5-year": 500, "10-year": 400 },
    { rainfallDuration: 100, "2-year": 900, "5-year": 800, "10-year": 600 },
];


export default function IDFChart() {

    return (
        <div
            className="IDFChart"
            style={{ display: "flex", flexDirection: "column", alignItems: "center", height: "400px" }}
        >
            <p>IDFChart</p>
            <LineChart width={1200} height={600} data={data}>
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis
                    dataKey="rainfallDuration"
                    type="number"
                    scale="log"
                    domain={[0.01, 100]}
                    ticks={[0.01, 0.02, 0.03, 0.04, 0.05, 0.06, 0.07, 0.08, 0.09, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100]}
                    label={{ value: 'Rainfall duration (h)', position: 'insideBottom', offset: -10 }}
                />


                <YAxis
                    type="number"
                    scale="log"
                    domain={[1, 1000]}
                    ticks={[1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 200, 300, 400, 500, 600, 700, 800, 900, 1000]}
                    label={{ value: 'Rainfall intensity (mm/h)', angle: -90, position: 'insideLeft', offset: 10 }}
                />
                <Tooltip></Tooltip>
                <Legend></Legend>
                <Line type="monotone" dataKey="2-year" stroke="green"></Line>
                <Line type="monotone" dataKey="5-year" stroke="red"></Line>
            </LineChart>
        </div>


    );
}