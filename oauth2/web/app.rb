require 'sinatra/base'
require 'slim'
require 'oauth2'
require 'json'

class App < Sinatra::Base
  TAB_URL = 'http://localhost:3030/'
  REDIRECT_URI = 'http://localhost:9292/oauth2_callback'
  CLIENT_ID = '166b5d67df47edb64235b59961953fc9'
  CLIENT_SECRET = '3a6c7e5f092d6ef8d13fda81e080e974'

  def oauth2_client
    OAuth2::Client.new(CLIENT_ID, CLIENT_SECRET, site: TAB_URL, authorize_url: '/oauth2/authorize', token_url: '/api/1/oauth2/token')
  end

  configure do
    #set :root, File.expand_path('../../', __FILE__)
  end

  # http://www.sinatrarb.com/contrib/reloader
  configure :development do
    Bundler.require :development
    register Sinatra::Reloader
  end

  get '/' do
    slim :index, locals: {tab_url: TAB_URL, redirect_uri: REDIRECT_URI, client_id: CLIENT_ID}
  end

  get '/start_oauth2' do
    redirect oauth2_client.auth_code.authorize_url(redirect_uri: REDIRECT_URI)
  end

  get '/oauth2_callback' do
    if params[:error]
      return "#{params[:error]}: #{params[:error_description]}"
    end
    access_token = oauth2_client.auth_code.get_token(params[:code], redirect_uri: REDIRECT_URI)
    resp = access_token.get('/api/1/users/me')
    me = JSON.parse(resp.body)["user"]
    slim :oauth2_callback, locals: { id: me["id"], screen_name: me["screen_name"], image_url: me["profile_image_url"]["crop_M"]}
  end
end
