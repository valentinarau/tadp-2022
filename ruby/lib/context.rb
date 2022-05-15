class Context

  def initialize(instance, params, *args)
    @self = instance
    params.each_index do |i|
      name = params[i][1]
      define_singleton_method(name) do
        args[i]
      end
    end
  end

end