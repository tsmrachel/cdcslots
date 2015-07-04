
// screenscrape.js

var webdriverioClass = require('webdriverio');
var cheerio = require('cheerio');
var Promise = require('promise');


var options = {
  desiredCapabilities: {
    browserName: 'phantomjs'
  }
};

var range;
var nextPage;
var timeslots = [];
var element;


var sessionFilter = [];
var dayFilter = ["SUN","SAT"];

function extractTimeSlots (timeslots,source){

 $ = cheerio.load(source);

 range = $('#ctl00_ContentPlaceHolder1_lblSesTitle').text();
 nextPage = $('#ctl00_ContentPlaceHolder1_lblNextdays').text();   


 console.log(nextPage);
 console.log(range);

 $ = cheerio.load($('#ctl00_ContentPlaceHolder1_gvLatestav').html());


 var row;
 var cell;
 var tempArray = [];

 var skip = false;

 $('tr').each(function(i, elem) {

  skip = false;


  row = cheerio.load(elem);

  row('td').each(function(i,elem) {


    cell = cheerio.load(elem);

    if (i===0){

      tempArray[i] = cell('td').text();

    }

    else if (i ===1) {


            // filter by days 

            if (dayFilter.indexOf(cell('td').text()) >= 0){

              tempArray[i] = cell('td').text();

            }

            else{

              skip = true;

              return false;
            }
          }

          else{


            if (cell('td input').attr('src') === "../Images/Class3/Images1.gif"){

              tempArray[i] = cell('td input').attr('id').substr(-8);

            }
          }

        });

  console.log(tempArray);

  // if there is a day which you are interested in and there is a session available

  if ((!skip) && (tempArray.length > 1)){

    timeslots.push(tempArray);
  }


  tempArray = [];

});

        // console.log(timeslots);

      };



      function scrapeNextPage(){

        return new Promise(function(resolve,reject){

          webdriverio = webdriverio.getSource(function(err, source) {

            extractTimeSlots (timeslots,source);

            if (nextPage !== 'Not Available') {
            //yay more stuff to scrape
            element = '\/\/span[@id=\'ctl00_ContentPlaceHolder1_lblSesTitle\' and not(text()[contains(.,\'' + range + '\')])]';
            webdriverio = webdriverio
            .waitForVisible('#ctl00_ContentPlaceHolder1_btnFrwArrow',10000)
            .click('#ctl00_ContentPlaceHolder1_btnFrwArrow')
            .waitForExist(element,10000);


            scrapeNextPage().then(function(){

              resolve();

            });


          } else {
            //all done
            console.log("scrapeNextPage done");
            webdriverio.end();
            resolve();

          }
        });

        });




      };


      var screenscrape = function() {

        return new Promise(function(resolve,reject){

          webdriverio = webdriverioClass.remote(options).init();


          webdriverio = webdriverio
          .url('http://www.cdc.com.sg/Portal/login.aspx')
          .setValue('#Header1_Login1_txtUserName', <username>)
          .setValue('#Header1_Login1_txtPassword', <password>)
          .click('#Header1_Login1_btnLogin')
          .click('#ctl00_Menu1_TreeView1t3')
          .selectByVisibleText('#ctl00_ContentPlaceHolder1_ddlCourse','Class 3 (Manual Vios) Practical & Theory - CDC Ubi')
          .waitForExist('#ctl00_ContentPlaceHolder1_gvLatestav_ctl02_btnSession1',10000);


          scrapeNextPage().then(function(){
            resolve();
          });

        });

      };

// end screenscrap.js



var express = require('express');
var app = express();

var request = require('request');

var bodyParser = require('body-parser');


var cors = require('cors');


//app.use(bodyParser()); deprecated - provide extended value
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({
  extended: true
}));



app.use(cors());



app.get('/getSlots',function(req,res){

  console.log("in timeslots function");

  timeslots = [];

  screenscrape().then(function(){
      // res.json
      res.send(timeslots);
    });

});



app.listen(3000);
console.log('Listening on port 3000');

