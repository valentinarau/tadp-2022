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

  private def method_missing(symbol, *args)
    @self.method(symbol).call *args
  end

  def execute(exception = ValidationError, &block)
    unless block.nil?
      raise exception.new unless self.instance_eval &block
    end
  end

  def execute_before
    @self.class
         .instance_variable_get(:@global_before)
         .each { |block| Class.instance_exec(ValidationError, &block) }
  end

  def execute_after
    @self.class
         .instance_variable_get(:@global_after)
         .each { |block| Class.instance_exec(ValidationError, &block) }
  end
end