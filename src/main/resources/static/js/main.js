

$(document).ready(function (){


    $('.eBtn').on('click', function (event){

        event.preventDefault();
        const href = $(this).attr('href');

        $.get(href, function (user){


            $('.myForm #userId').val(user.userId)
            $('.myForm #userName').val(user.userName);
            $('.myForm #userAge').val(user.userAge);
            $('.myForm #password').val(user.password)

            $.each(user.userRoles, function (index, value){
                if(value === "ROLE_ADMIN"){
                    $('.selDiv option[value="2"]').prop('selected', true);
                }
                if(value === "ROLE_USER"){
                    $('.selDiv option[value="1"]').prop('selected', true);
                }
            })


        });
        $('#exampleModal').modal();
    });

});

