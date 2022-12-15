function table_row(data){
    let str = `<td>${data.titel}</td><td>${data.content}</td><td>${data.updated}</td> `;
    let tr = document.createElement("tr");
    tr.innerHTML = str;
    return tr;
}

function showTable(data){
    console.log(data);
    let eingabelist = document.getElementById("eingabeliste")
    for(let e of data){
        let tr = table_row(e);
        eingabelist.appendChild(tr);
    }
}


function load(){
    fetch("/api/product").then(function(result){
        console.log(result);
        if(result.ok){
            result.json().then(function(data){
                console.log(data);
                showTable(data);
            });
        }else{
            console.log("Error: "+result.status);
        }})
}
document.addEventListener("DOMContentLoaded", load);