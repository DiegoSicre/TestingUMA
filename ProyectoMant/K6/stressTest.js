import http from 'k6/http';

export const options = {
    stages: [
        { duration: '3m', target: 6777 },
        { duration: '3m', target: 6777 },
        { duration: '2m', target: 0 },
    ],
    thresholds: {
        http_req_failed: [{ threshold: 'rate<=0.01', abortOnFail: true }],
        http_req_duration: ['avg<1000'],
    },
};

export default function () {
    http.get('http://localhost:8080/');
}