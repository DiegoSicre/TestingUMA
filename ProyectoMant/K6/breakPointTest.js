import http from 'k6/http';

export const options = {
    stages: [
        { duration: '10m', target: 100000 },
    ],
    thresholds: {
        http_req_failed: [{ threshold: 'rate<=0.01', abortOnFail: true }],
    },
};

export default function () {
    http.get('http://localhost:8080/');
}