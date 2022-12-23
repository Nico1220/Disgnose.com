function table_row(data){
    let str = `<td>${data.email}</td><td>${data.firstName}</td><td>${data.lastName}</td><td>${data.roles}</td> `;
    let tr = document.createElement("tr");
    tr.innerHTML = str;
    return tr;
}

function showTable(data){
    console.log(data);
    let eingabelist = document.getElementById("users")
    for(let e of data){
        let tr = table_row(e);
        eingabelist.appendChild(tr);
    }
}


function load(){
    fetch("/api/user").then(function(result){
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