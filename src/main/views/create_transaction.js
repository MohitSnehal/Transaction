$(function () {

    $('#submit-btn-create').on('click', function (e) {

        var transactionId =  $('#transaction_id_create').val();
        var amount =  $('#amount_create').val();
        var type =  $('#type_create').val();
        var parentId =  $('#parent_id_create').val();
        var requestBody = {
            "amount" : amount,
            "type" : type,
            "parentId" : parentId
        };
        var headers =  {
            "Content-Type": "application/json",
            "Access-Control-Allow-Origin": "*",
            "Access-Control-Allow-Methods": "PUT"
        };
        var url = "http://localhost:8080/transactionservice/transaction/" + (transactionId ? " " : transactionId);
        console.log("Xxxxxxxxxx",url);
        fetch(url, {
            method: 'PUT',
            headers:headers,
            body: JSON.stringify(requestBody)
        })
            .then(response => response.json())
            .then(function (response){
                alert(response);
            });

    })
});

$(function () {

    $('#transaction_id_create').on("keydown", function (e) {
        if(e.key === 'Enter') {
            var transactionId =  $('#transaction_id_create').val();
            var url = "http://localhost:8080/transactionservice/transaction/" + transactionId;
            console.log("Xxxxxxxxxx",url);
            fetch(url)
                .then(response => response.json())
                .then(function (response){
                    let data = response.payload;
                    console.log(data);
                    document.getElementById("transaction_id_create").value = (data.id);
                    document.getElementById("amount_create").value = (data.amount);
                    document.getElementById("type_create").value =  (data.type);
                    document.getElementById("parent_id_create").value = (data.parentId);
                });
        }
    })
});