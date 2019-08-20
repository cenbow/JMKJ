$(function() {
  $('.tab').on('click','span',function(index){
    $(this).addClass('active')
    $(this).siblings().removeClass('active')
    $('.line').css('left', $(this).index()*33.33 + '%')
    let lest = ['layer1','layer2','layer3']
    lest.forEach((item,index)=>{
      $('.' + lest[index]).css('display','none')
    })
    $('.layer0').css('display','none')
    if($(this).index() == 1){
      $('.' + lest[$(this).index()]).css('display','flex')
    }else{
      $('.' + lest[$(this).index()]).css('display','block')
    }
  })
})
