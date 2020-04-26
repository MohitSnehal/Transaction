$(function () {

    $('#submit-btn-get-by-type').on('click', function (e) {

        var type =  $('#transaction_type').val();
        var pageNumber = $('#page_number').val();
        var pageSize = $('#page_size').val();
        var url = "http://localhost:8080/transactionservice/type/" + type;
        if(pageNumber && pageSize){
            console.log("adding page number and page size");
            url += "?pageNumber=" + pageNumber + "&pageSize=" + pageSize;
        }
        fetch(url)
            .then(response => response.json())
            .then(function (response){
                if(!response.success) {
                    alert(response.payload.details);
                } else{
                    let data = response.payload.transactionResponses;
                    $("#table_type").find("tr:gt(0)").remove();
                    data.forEach(function (element, i) {
                        addRow(0,element.id,element.amount,element.type,element.parentId);
                    });
                }
            });

    })
});

$(function () {

    $('#transaction_type').on('keydown', function (e) {
        if(e.key === 'Enter') {
            var type =  $('#transaction_type').val();
            var url = "http://localhost:8080/transactionservice/type/" + type;
            fetch(url)
                .then(response => response.json())
                .then(function (response){
                    if(!response.success) {
                        alert(response.payload.details);
                    } else{
                        let data = response.payload.transactionResponses;
                        $("#table_type").find("tr:gt(0)").remove();
                        data.forEach(function (element, i) {
                            addRow(0,element.id,element.amount,element.type,element.parentId);
                        });
                    }
                });
        }
    })
});

function addRow(index,id,amount,type,parentId)
{
    if (!document.getElementsByTagName) return;
    tabBody=document.getElementsByTagName("tbody").item(index);
    row=document.createElement("tr");
    cell1 = document.createElement("td");
    cell2 = document.createElement("td");
    cell3 = document.createElement("td");
    cell4 = document.createElement("td");
    textnode1=document.createTextNode(id);
    textnode2=document.createTextNode(amount);
    textnode3=document.createTextNode(type);
    textnode4=document.createTextNode(parentId);
    cell1.appendChild(textnode1);
    cell2.appendChild(textnode2);
    cell3.appendChild(textnode3);
    cell4.appendChild(textnode4);
    row.appendChild(cell1);
    row.appendChild(cell2);
    row.appendChild(cell3);
    row.appendChild(cell4);
    tabBody.appendChild(row);
}