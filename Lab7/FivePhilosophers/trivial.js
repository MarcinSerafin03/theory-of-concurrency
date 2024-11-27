const philosophers  = 5;
const forks = Array(philosophers).fill(false);

function think(philosopher) {
    console.log(`Philosopher ${philosopher} is thinking.`);
    setTimeout(() => eat(philosopher), Math.random() * 10);
}

function eat(philosopher){
    console.log(`Philosopher ${philosopher} wants to eat`);
    const leftFork = philosopher;
    const rightFork = (philosopher + 1) % philosophers;
    setTimeout(() => {
        if(!forks[leftFork] && !forks[rightFork]){
            forks[leftFork] = true;
            forks[rightFork] = true;
            console.log(`Philosopher ${philosopher} is eating`);
            setTimeout(() => {
                forks[leftFork] = false;
                forks[rightFork] = false;
                think(philosopher);
            }, Math.random() * 10);
        }
        else{
            setTimeout(() => eat(philosopher), Math.random() * 10);
        }
    }, Math.random() * 1000);
}


function start(){
    for(let i = 0; i < philosophers; i++){
        think(i);
    }
}

start();