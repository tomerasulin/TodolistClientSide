

function getListView() {        // load the list view from DB and waiting for events like edit ore delete item
//
    var json;
    var toDoList;

    var ul= document.getElementById("todoList");    // loading todo list
    json = JSON.parse(window.getDataFromDB.getItems());     // pass data from DB to json file.
    toDoList = json.toDoItems;
    for(let i=0; i<toDoList.length ; i++)   // pass over all the items from DB and create li item
    {
        var li = document.createElement("li");

        date = toDoList[i].date;
        item = toDoList[i].item;
        li.appendChild(document.createTextNode(item + " " + date));
        var delbutt=document.createElement("button");
        delbutt.appendChild(document.createTextNode("Delete"));
        delbutt.setAttribute("data-icon","delete");
        delbutt.id = date +"," + item;      // take data and save - must for search on DB

        var updatebutt = document.createElement("button");
        updatebutt.appendChild(document.createTextNode("Edit"));
        updatebutt.setAttribute("data-icon","edit");
        updatebutt.id = date +"," + item;       // take data and save - must for search on DB

        li.appendChild(updatebutt);
        li.appendChild(delbutt);

        ul.appendChild(li);

        updatebutt.addEventListener("click", function(){    // waiting to edit button
            var splitDteItem = this.id.split(',');
            window.location.href = '#update';       // move to update page

            $("#updateButton").click(function() {
                var updateDate = document.getElementById("newDateValue").value;     // get data from "update" page
                var updateItem = document.getElementById("newNameValue").value;
                window.updateDB.updateToDo(splitDteItem[0],splitDteItem[1],updateDate,updateItem);  //call to DB function - update/edit the item
                location.reload();      // reload todo list

            });
        });

        delbutt.addEventListener("click", function(){       // waiting to delete button
            this.parentElement.parentElement.remove();      // remove immediately from list by removing "parent" - li
            var splitDteItem = this.id.split(',');
            window.deleteToDoFromDb.deleteToDo(splitDteItem[0],splitDteItem[1]);        // call to DB function - delete todo from DB
        });

    }
}


function updateList(date,item){ // every time we add new item this function add the item to list dynamically


    var ul= document.getElementById("todoList");
    var li = document.createElement("li");

    li.appendChild(document.createTextNode(item + " " + date));

    var delbutt=document.createElement("button");
    delbutt.appendChild(document.createTextNode("Delete"));
    delbutt.setAttribute("data-icon","delete");
    delbutt.id = date +"," + item;

    var updatebutt = document.createElement("button");
    updatebutt.appendChild(document.createTextNode("Edit"));
    updatebutt.setAttribute("data-icon","edit");
    updatebutt.id = date +"," + item;

    li.appendChild(delbutt);
    li.appendChild(updatebutt);

    ul.appendChild(li);
//    $("#todoList").listview("refresh");

    updatebutt.addEventListener("click", function(){
        var splitDteItem = this.id.split(',');
        window.location.href = '#update';

        $("#updateButton").click(function() {
            var updateDate = document.getElementById("newDateValue").value;
            var updateItem = document.getElementById("newNameValue").value;
            window.updateDB.updateToDo(date,item,updateDate,updateItem);
            location.reload();

        });
    });

    delbutt.addEventListener("click", function(){
        this.parentElement.parentElement.remove();
        window.deleteToDoFromDb.deleteToDo(date,item);
    });


}

function add() {    //adding new item to DB and then we update the list again.

    var date = document.getElementById("new_date").value;
    var item = document.getElementById("new_item").value;
    window.addToDb.addItem(date,item);
    updateList(date,item);
    location.reload();

}



