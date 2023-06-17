
$('.hint').click(function() {
  $(this).toggleClass("show");
});

function spinnerOverlay() {
    document.getElementById("loader").removeAttribute("hidden");
    document.getElementById("loader2").removeAttribute("hidden");
    document.getElementById("loader3").removeAttribute("hidden");
};

function printTable() {
  let envStatsTable = document.getElementById('envStatsTable');
  var reload = document.getElementById('reload');
  reload.remove();
  var spinner = document.getElementById('spinner');
  spinner.classList.add("spinner-border");
  spinner.classList.add("text-primary");
  if(envStatsTable){
      var tableArray = new Array(3);
      tableArray[0] = document.getElementById('env').innerText.substring(13,15);
      var counter = 1;
      for (let row of envStatsTable.rows) {
            tableArray[counter++] = row.cells[0].innerText;
      }

      var ajaxRequest = () => {
                                  $.ajax({
                                  url: '/environmentReload',
                                  type: 'POST',
                                  contentType: 'application/json',
                                  data: JSON.stringify(tableArray),
                                  success: function(data){
                                    var jsonObj = JSON.parse(data);
                                    jsonObj.forEach(({name, state, componentType}) => {
                                        var id = componentType + name;
                                        document.getElementById(id).innerText = state;
                                    });
                                    document.getElementById('timeStamp').innerText = 'Last Refreshed at ' + new Date();
                                  },
                                  error: function(){
                                      alert('error');
                                  }
                              });
                          }
      setInterval(ajaxRequest, 10000);
  }
}


