import http from 'k6/http';

export const options = {
    stages: [
        { duration: '3m', target: 4563 },
        { duration: '3m', target: 4563 },
        { duration: '2m', target: 0 },
    ],
    thresholds: {
        http_req_failed: [{ threshold: 'rate<=0.01', abortOnFail: true }],
    },
};

export default function () {
    http.get('http://localhost:8080/medico/1');
}
