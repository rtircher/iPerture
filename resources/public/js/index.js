(function() {

  var Image = Backbone.Model.extend(),

      ImagesList = Backbone.Collection.extend({
        model: Image
      }),

      ImageView = Backbone.View.extend({
        tagName: 'li',
        click: 'imageClicked',
        imageClicked: function() {
          alert('image view');
        }
      }),
      ThumbnailView = Backbone.View.extend({
        tagName: 'li',
        click: 'thumbnailClicked',
        thumbnailClicked: function() {
          alert('thumbnail view');
        }
      }),

      ThumbnailsView = Backbone.View.extend({
        className: 'thumbnails-list'
      }),
      ImagesView = Backbone.View.extend({
        className: 'images-list'
      });

})();