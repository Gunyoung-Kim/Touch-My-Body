//이번달 날짜
let date = new Date();

let current;

let isDone;

const renderCalendar = () => {

  const viewYear = date.getFullYear();
  const viewMonth = date.getMonth();

  document.querySelector(".year-month").textContent = `${viewYear}년 ${viewMonth+1}월`;

  const prevLast = new Date(viewYear, viewMonth, 0);
  const thisLast = new Date(viewYear, viewMonth+1,0);

  const PLDate = prevLast.getDate();
  const PLDay = prevLast.getDay();

  const TLDate = thisLast.getDate();
  const TLDay = thisLast.getDay();

  const prevDates = [];
  const thisDates = [...Array(TLDate + 1).keys()].slice(1);
  const nextDates = [];

  if(PLDay !== 6) {
    for (let i=0;i<PLDay+1;i++) {
      prevDates.unshift(PLDate -i);
    }
  }

  for(let i=1;i< 7 -TLDay;i++) {
    nextDates.push(i);
  }

  const dates = prevDates.concat(thisDates, nextDates);

  const firstDateIndex = dates.indexOf(1);
  const lastDateIndex = dates.lastIndexOf(TLDate);
  dates.forEach((dateNum,i) => {
    const condition = i >= firstDateIndex && i <= lastDateIndex +1 ? 'this' : 'other';

    dates[i] = `<div class="date" id="date${dateNum}" onclick="getRecords(${dateNum})"><span class="${condition}">${dateNum}</span></div>`;
  })

  document.querySelector('.dates').innerHTML = dates.join('');

  const today = new Date();
  if (viewMonth === today.getMonth() && viewYear === today.getFullYear()) {
    for (let date of document.querySelectorAll('.this')) {
      if (+date.innerText === today.getDate()) {
        date.classList.add('today');
        break;
      }
    }
  }
}

renderCalendar();

const prevMonth = () => {
  date.setMonth(date.getMonth() -1);
  renderCalendar();
}

const nextMonth = () => {
  date.setMonth(date.getMonth() +1);
  renderCalendar();
}

const goToday = () => {
  date = new Date();
  renderCalendar();
}

const goToAddRecords = () => {
  location.href = '/user/exercise/calendar/addrecord';
}



const getRecords = (dateNum) => {

  $('#recordsList').empty();

  $.ajax({
    url: '/user/exercise/calendar/records',
    method: 'GET',
    data: {"year" : date.getFullYear(), "month" : date.getMonth(), "date" : dateNum},

    success:function(data) {
      console.log(data);
      if(data.length==0) {
        console.log("결과가 없습니다.");
      } else {
        $.each(data, function() {
          $('#recordsList').append(
            `
            <tr>
            <td colspan="2">${this.exerciseName}</td>
            <td>${this.weight}</td>
            <td>${this.sets}</td>
            <td>${this.laps}</td>
            <td colspan="2">${this.description}</td>
            </tr>
            `);
        })
      }
    }
  })
}

const getIsDone = () => {

}
