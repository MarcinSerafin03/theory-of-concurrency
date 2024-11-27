var Fork = function() {
    this.state = 0;
    return this;
}

Fork.prototype.acquire = function (cb) {
    const tryAcquire = (delay) => {
        if (this.state === 0) {
            this.state = 1;
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

Philosopher.prototype.startNaive = function (count) {
    const eatCycle = (i) => {
        if (i >= count) return;

        this.forks[this.f1].acquire(() => {
            this.forks[this.f2].acquire(() => {
                console.log(`Philosopher ${this.id} is eating`);
                setTimeout(() => {
                    // Release both forks after eating
                    this.forks[this.f1].release();
                    this.forks[this.f2].release();
                    console.log(`Philosopher ${this.id} finished eating`);
                    eatCycle(i + 1); // Start next cycle
                }, Math.random() * 100); // Simulate eating time
            });
        });
    };

    eatCycle(0);
};

Philosopher.prototype.startAsym = function (count) {
    const eatCycle = (i) => {
        if (i >= count) return;

        const first = this.id % 2 === 0 ? this.f1 : this.f2;
        const second = this.id % 2 === 0 ? this.f2 : this.f1;

        this.forks[first].acquire(() => {
            this.forks[second].acquire(() => {
                console.log(`Philosopher ${this.id} is eating`);
                setTimeout(() => {
                    // Release both forks after eating
                    this.forks[first].release();
                    this.forks[second].release();
                    console.log(`Philosopher ${this.id} finished eating`);
                    eatCycle(i + 1); // Start next cycle
                }, Math.random() * 100); // Simulate eating time
            });
        });
    };

    eatCycle(0);
};

var Conductor = function (limit) {
    this.limit = limit;
    this.current = 0;
};

Conductor.prototype.acquire = function (cb) {
    const tryAcquire = (delay) => {
        if (this.current < this.limit) {
            this.current++;
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

Philosopher.prototype.startConductor = function (count) {
    const eatCycle = (i) => {
        if (i >= count) return;

        conductor.acquire(() => {
            this.forks[this.f1].acquire(() => {
                this.forks[this.f2].acquire(() => {
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


var N = 5;
var forks = [];
var philosophers = []
for (var i = 0; i < N; i++) {
    forks.push(new Fork());
}

for (var i = 0; i < N; i++) {
    philosophers.push(new Philosopher(i, forks));
}

for (var i = 0; i < N; i++) {
    philosophers[i].startNaive(1000);
}

// for (var i = 0; i < N; i++) {
//     philosophers[i].startAsym(10);
// }

// for (var i = 0; i < N; i++) {
//     philosophers[i].startConductor(10);
// }
