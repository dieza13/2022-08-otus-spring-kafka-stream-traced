const passToHandling = (contract) => {
    return fetch('/api/contract',
        {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(contract),
        })
        .then(response => response.json());
}

const getStatus = id => {
    return fetch('/api/contract/' + id + '/status').then(response => response.json());
}


const ContractService = {
    passToHandling,
    getStatus
}

export default ContractService;