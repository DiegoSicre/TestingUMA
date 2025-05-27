import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
    vus: 5,
    duration: '1m',
    thresholds: {
        http_req_failed: ['rate<=0.00'],
        http_req_duration: ['avg<500'],
    },
};

export default function () {
    const res = http.get('http://localhost:8080/medico/1');
    check(res, {
        'status is 200': (r) => r.status === 200,
    });
    sleep(1);
}
