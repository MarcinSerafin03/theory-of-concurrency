const philosophers  = 5;
const forks = Array(philosophers).fill(false);
const waiter = {count: 0,max : philosophers - 1};

function think(philosopher) {
    console.log(`Philosopher ${philosopher} is thinking.`);
    setTimeout(() => eat(philosopher), Math.random() * 1000);
}

function eat(philosopher){
    console.log(`Philosopher ${philosopher} wants to eat`);
    const leftFork = philosopher;
    const rightFork = (philosopher + 1) % philosophers;

    setTimeout(() =>{
        if(waiter.count < waiter.max && !forks[leftFork] && !forks[rightFork]){
            waiter.count++;
            forks[leftFork] = true;
            forks[rightFork] = true;
            console.log(`Philosopher ${philosopher} is eating`);
            setTimeout(() => {
                forks[leftFork] = false;
                forks[rightFork] = false;
                waiter.count--;
                think(philosopher);
            }, Math.random() * 1000);
        }
        else{
            setTimeout(() => eat(philosopher), Math.random() * 1000);
        }
    }, Math.random() * 1000);
}

function start(){
    for(let i = 0; i < philosophers; i++){
        think(i);
    }
}

start();