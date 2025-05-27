import http from 'k6/http';

export const options = {
    stages: [
        { duration: '2m', target: 3650 },
        { duration: '2m', target: 0 },
    ],
    thresholds: {
        http_req_failed: [{ threshold: 'rate<=0.005', abortOnFail: true }],
    },
};

export default function () {
    http.get('http://localhost:8080/medico/1');
}
