$(document).ready( function () {

    var theses = $('#shoppings').DataTable({
        "sAjaxSource": "/shoppings",
        "sAjaxDataProp": "",
        "order": [[ 0, "asc" ]],
        language: {
            url: '//cdn.datatables.net/plug-ins/1.10.19/i18n/French.json'
        },
        "aoColumns": [
            { "mData": "name"},
            { "mData": "comment" },
            { "mData": "date" },

            { "mData": "id_shop",
                mRender: function (mData,type,row){
                    var str3='';
                    str3 += '<a href="/'+mData+'" class="btn">Ouvrir&nbsp;&nbsp;<i class="fa fa-eye"></i></a>';
                    str3 += '<a href="edit/'+mData+'" class="btn">Editer&nbsp;&nbsp;<i class="fa fa-pencil"></i></a>';
                    return str3;
                }}

        ]
    })
    var these = $('#shopping').DataTable({

    })

});