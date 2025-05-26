import http from 'k6/http';
import {sleep} from 'k6';

export const options = {
    vus: 5,
    duration: '1m',
    thresholds: {
        http_req_failed: ['rate<=0.00'], // sin errores
        http_req_duration: ['avg<500'],  // duración media < 500ms
    },
};

export default function () {
    const res = http.get('http://localhost:8080/medico/1');
    sleep(1);
}
