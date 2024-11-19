function printAsync(s) {
    return new Promise((res) => {
        var delay = Math.floor((Math.random() * 1000) + 500);
        setTimeout(() => {
            console.log(s);
            res();
        }, delay);
    });
}

async function printAll() {
    await printAsync("1");
    await printAsync("2");
    await printAsync("3");
    console.log('done!');
}

printAll();