module Contrato

  def invariant(&block)
    if @invariants.nil?
      @invariants = []
    end
    @invariants << block
  end

  def before_and_after_each_call(block_before, block_after)
    @global_before ||= []
    @global_after ||= []
    @global_before << block_before
    @global_after << block_after
  end

  def pre(&block)
    @block_pre = block
  end

  def post(&block)
    @block_post = block
  end

  private def should_skip(method_name)
    method_name.eql?(:method_added) or
      (@invariants.nil? &&
        @block_post.nil? && @block_pre.nil? &&
        @global_before.nil? && @global_after.nil?)
  end

  private def init_if_needed
    if @wrapped_methods.nil? && @method_blocks.nil?
      @wrapped_methods = []
      @method_blocks = {}
    end
    if @global_before.nil? && @global_after.nil?
      @global_after = []
      @global_before = []
    end

  end

  private def extract_method_data
    data = { pre: @block_pre, post: @block_post, before: @global_before, after: @global_after }
    @block_pre = nil
    @block_post = nil
    data
  end

  def check_invariants(instance)
    unless @invariants.nil?
      @invariants.each do |invariant|
        unless instance.instance_eval &invariant
          raise InvariantError.new
        end
      end
    end
  end
end