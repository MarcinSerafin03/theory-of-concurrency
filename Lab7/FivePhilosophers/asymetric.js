const philosophers  = 5;
const forks = Array(philosophers).fill(false);

function think(philosopher) {
    console.log(`Philosopher ${philosopher} is thinking.`);
    setTimeout(() => eat(philosopher), Math.random() * 1000);
}


function eat(philosopher){
    console.log(`Philosopher ${philosopher} wants to eat`);
    const leftFork = philosopher;
    const rightFork = (philosopher + 1) % philosophers;

    const firstFork = philosopher % 2 === 0 ? leftFork : rightFork;
    const secondFork = philosopher % 2 === 0 ? rightFork : leftFork;
    setTimeout(() => {
        if(!forks[firstFork] && !forks[secondFork]){
            forks[firstFork] = true;
            forks[secondFork] = true;
            console.log(`Philosopher ${philosopher} is eating`);
            setTimeout(() => {
                forks[firstFork] = false;
                forks[secondFork] = false;
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