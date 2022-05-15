module Contrato

  def invariant(&block)
    if @invariants.nil?
      @invariants = []
    end
    @invariants << block
  end

  def pre(&block)
    @block_pre = block
  end

  def post(&block)
    @block_post = block
  end

  private def should_skip(method_name)
    method_name.eql?(:method_added) or
      (@invariants.nil? && @block_post.nil? && @block_pre.nil?)
  end

  private def init_if_needed
    if @wrapped_methods.nil? && @method_blocks.nil?
      @wrapped_methods = []
      @method_blocks = {}
    end
  end

  private def execute(&block)
    unless block.nil?
      block.call
    end
  end

  def check_invariants
    unless @invariants.nil?
      @invariants.each do |invariant|
        unless invariant.call
          # raise Exception
          puts "Rompe invariant"
        end
      end
    end
  end
end

class Module
  include Contrato

  def method_added(method_name)
    if should_skip(method_name)
      return
    end

    init_if_needed
    if @wrapped_methods.include?(method_name) # el metodo ya fue wrappeado y se está redefiniendo
      @wrapped_methods.delete(method_name)
    else # el metodo se está definiendo por primera vez o redefiniendo después de wrappearlo
      @wrapped_methods << method_name
      @method_blocks[method_name] = { pre: @block_pre, post: @block_post }
      @block_pre = nil
      @block_post = nil
      orig_meth = instance_method(method_name)
      method_data = @method_blocks[method_name]
      check = method(:check_invariants)
      exec = lambda { |&block| execute &block } # bring execute to inner instance scope
      define_method(method_name) do |*args, &block|
        exec.call &method_data[:pre]
        orig_meth.bind(self).call *args, &block
        exec.call &method_data[:post]
        check.call
      end
    end
  end
end