export const options = {
    scenarios: {
        breakingPoint: {
            executor: 'ramping-arrival-rate',
            startRate: 100,
            timeUnit: '1s',
            preAllocatedVUs: 5000,
            maxVUs: 100000,
            stages: [
                { duration: '10m', target: 100000 },
            ],
        },
    },
    thresholds: {
        http_req_failed: [{ threshold: 'rate<=0.01', abortOnFail: true }],
    },
};

export default function () {
    http.get('http://localhost:8080/medico/1');
}
