import {createRequire} from 'module';
const require = createRequire(import.meta.url);
const fs = require('fs');
const axios = require('axios');
const QuickChart = require('quickchart-js');
var Fork = function() {
    this.state = 0;
    return this;
}

Fork.prototype.acquire = function (philosopherId, metrics, cb) {
    const startTime = Date.now();
    const tryAcquire = (delay) => {
        if (this.state === 0) {
            this.state = 1;
            metrics.waitingTime[philosopherId] += Date.now() - startTime; // Czas oczekiwania
            cb();
        } else {
            setTimeout(() => tryAcquire(delay * 2), delay);
        }
    };
    tryAcquire(1);
}


Fork.prototype.release = function() { 
    this.state = 0; 
}

var Philosopher = function(id, forks) {
    this.id = id;
    this.forks = forks;
    this.f1 = id % forks.length;
    this.f2 = (id+1) % forks.length;
    return this;
}

Philosopher.prototype.startNaive = function (count,metrics) {
    const eatCycle = (i) => {
        if (i >= count) return;

        this.forks[this.f1].acquire(this.id,metrics,() => {
            this.forks[this.f2].acquire(this.id,metrics,() => {
                console.log(`Philosopher ${this.id} is eating`);
                setTimeout(() => {
                    this.forks[this.f1].release();
                    this.forks[this.f2].release();
                    console.log(`Philosopher ${this.id} finished eating`);
                    eatCycle(i + 1);
                }, Math.random() * 100);
            });
        });
    };

    eatCycle(0);
};

Philosopher.prototype.startAsym = function (count,metrics) {
    const eatCycle = (i) => {
        if (i >= count) return;

        const first = this.id % 2 === 0 ? this.f1 : this.f2;
        const second = this.id % 2 === 0 ? this.f2 : this.f1;

        this.forks[first].acquire(this.id,metrics,() => {
            this.forks[second].acquire(this.id,metrics,() => {
                console.log(`Philosopher ${this.id} is eating`);
                setTimeout(() => {
                    this.forks[first].release();
                    this.forks[second].release();
                    console.log(`Philosopher ${this.id} finished eating`);
                    eatCycle(i + 1);
                }, Math.random() * 100);
            });
        });
    };

    eatCycle(0);
};

var Conductor = function (limit) {
    this.limit = limit;
    this.current = 0;
};

Conductor.prototype.acquire = function (philosopherId,metrics,cb) {
    const startTime = Date.now();
    const tryAcquire = (delay) => {
        if (this.current < this.limit) {
            this.current++;
            metrics.waitingTime[philosopherId] += Date.now() - startTime;
            cb();
        } else {
            setTimeout(() => tryAcquire(delay * 2), delay); // Retry with double delay
        }
    };
    tryAcquire(1);
};

Conductor.prototype.release = function () {
    this.current--;
};

var conductor = new Conductor(4);

Philosopher.prototype.startConductor = function (count,metrics) {
    const eatCycle = (i) => {
        if (i >= count) return;

        conductor.acquire(this.id,metrics,() => {
            this.forks[this.f1].acquire(this.id,metrics,() => {
                this.forks[this.f2].acquire(this.id,metrics,() => {
                    console.log(`Philosopher ${this.id} is eating`);
                    setTimeout(() => {
                        this.forks[this.f1].release();
                        this.forks[this.f2].release();
                        conductor.release();
                        console.log(`Philosopher ${this.id} finished eating`);
                        eatCycle(i + 1);
                    }, Math.random() * 100);
                });
            });
        });
    };

    eatCycle(0);
};

const run = async(method,N,cycles) => {
    const forks = [];
    const philosophers = [];
    const metrics = {
        waitingTime: new Array(N).fill(0)
    };

    for (let i = 0; i < N; i++) {
        forks.push(new Fork());
    }

    for (let i = 0; i < N; i++) {
        philosophers.push(new Philosopher(i, forks));
    }

    philosophers.forEach(p => {
        p[method](cycles,metrics);
    });

    await new Promise(resolve => setTimeout(resolve, 1000 * cycles));
    return metrics.waitingTime;
}


const generateChart = async (data, labels, title, fileName) => {

    const chart = new QuickChart();
    chart.setConfig({
        type: 'bar',
        data: {
            labels: labels,
            datasets: [
                {
                    label: 'Czas oczekiwania (ms)',
                    data: data,
                    backgroundColor: 'rgba(75, 192, 192, 0.6)',
                    borderColor: 'rgba(75, 192, 192, 1)',
                    borderWidth: 1,
                },
            ],
        },
        options: {
            plugins: {
                title: {
                    display: true,
                    text: title,
                },
            },
            scales: {
                y: {
                    beginAtZero: true,
                },
            },
        },
    });

    chart.setWidth(800).setHeight(600).setBackgroundColor('white');

    // Pobieranie URL i zapisywanie obrazu
    const chartUrl = chart.getUrl();
    const response = await axios.get(chartUrl, { responseType: 'arraybuffer' });
    fs.writeFileSync(fileName, response.data);
    console.log(`Wykres zapisano jako ${fileName}`);
};

var N = 5;
const cycles = 10;

const naiveTimes = await run('startNaive',N,cycles);

const asymTimes = await run('startAsym',N,cycles);

const conductorTimes = await run('startConductor',N,cycles);

const labels = Array.from({ length: N }, (_, i) => `Filozof ${i + 1}`);
await generateChart(naiveTimes, labels, 'naive','naive.png');
await generateChart(asymTimes, labels, 'asym', 'asym.png');
await generateChart(conductorTimes, labels, 'conductor', 'conductor.png');




// for (var i = 0; i < N; i++) {
//     philosophers[i].startNaive(5);
// }

// for (var i = 0; i < N; i++) {
//     philosophers[i].startAsym(10);
// }

// for (var i = 0; i < N; i++) {
//     philosophers[i].startConductor(10);
// }
