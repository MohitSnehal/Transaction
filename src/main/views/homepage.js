$(function () {

    $('#submit-btn-get-by-id').on('click', function (e) {

        var transactionId =  $('#transaction_id').val();
        var url = "http://localhost:8080/transactionservice/transaction/" + transactionId;
        console.log("Xxxxxxxxxx",url);
        fetch(url)
            .then(response => response.json())
            .then(function (response){
                let data = response.payload;
                $('#result_transaction_id').html(data.id);
                $('#result_amount').html(data.amount);
                $('#result_type').html(data.type);
                $('#result_parent').html(data.parentId);
        });

    })
});

$(function () {

    $('#transaction_id').on("keydown", function (e) {
        if(e.key === 'Enter') {
            var transactionId =  $('#transaction_id').val();
            var url = "http://localhost:8080/transactionservice/transaction/" + transactionId;
            console.log("Xxxxxxxxxx",url);
            fetch(url)
                .then(response => response.json())
                .then(function (response){
                    let data = response.payload;
                    $('#result_transaction_id').html(data.id);
                    $('#result_amount').html(data.amount);
                    $('#result_type').html(data.type);
                    $('#result_parent').html(data.parentId);

                });
        }
    })
});