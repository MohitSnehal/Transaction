$(function () {

    $('#submit-btn-get-by-id').on('click', function (e) {

        var transactionId =  $('#transaction_id_for_sum').val();
        var url = "http://localhost:8080/transactionservice/sum/" + transactionId;
        console.log("Xxxxxxxxxx",url);
        fetch(url)
            .then(response => response.json())
            .then(function (response){
                let data = response.payload.sum;
                $('#sum_label').html(data);
            });

    })
});

$(function () {

    $('#transaction_id_for_sum').on("keydown", function (e) {
        if(e.key === 'Enter') {
            var transactionId =  $('#transaction_id_for_sum').val();
            var url = "http://localhost:8080/transactionservice/sum/" + transactionId;
            console.log("Xxxxxxxxxx",url);
            fetch(url)
                .then(response => response.json())
                .then(function (response){
                    let data = response.payload;
                    $('#sum_label').html(data);

                });
        }
    })
});